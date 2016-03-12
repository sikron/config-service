package com.skronawi.configservice.core;

import com.skronawi.configservice.api.PropertyConversionException;
import com.skronawi.keyvalueservice.api.KeyNotExistingException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PropertyReadAccessTest {

    private DummyKeyValueStore keyValueStore;
    private PropertyReadAccessImpl propertyReadAccess;

    @BeforeClass
    public void setup() throws Exception {

        keyValueStore = new DummyKeyValueStore();
        keyValueStore.set("string", "asdf1234!§$");
        keyValueStore.set("int", "6");
        keyValueStore.set("double", "6.9");
        keyValueStore.set("boolean", "false");

        propertyReadAccess = new PropertyReadAccessImpl(keyValueStore);
    }

    @Test
    public void testGetKeys() throws Exception {
        Assert.assertEquals(propertyReadAccess.getKeys(), keyValueStore.getKeys());
    }

    @Test(expectedExceptions = KeyNotExistingException.class)
    public void testGetNonExistingKey() throws Exception {
        propertyReadAccess.getByKey("non-existing");
    }

    @Test
    public void testGetAsString() throws Exception {
        Assert.assertEquals(propertyReadAccess.getByKey("int").getValue(), "6");
    }

    @Test
    public void testGetExistingWithType() throws Exception {
        Assert.assertEquals(propertyReadAccess.getIntByKeyOrDefault("int", 0).getValue(), new Integer(6));
    }

    @Test
    public void testGetDefaultForNonExisting() throws Exception {
        Assert.assertEquals(propertyReadAccess.getIntByKeyOrDefault("non-existing", 3).getValue(), new Integer(3));
    }

    @Test(expectedExceptions = PropertyConversionException.class)
    public void testGetExistingWithWrongType() throws Exception{
        propertyReadAccess.getIntByKeyOrDefault("boolean", 4);
    }

    @Test
    public void testPropertyConversionException() throws Exception{
        try {
            propertyReadAccess.getIntByKeyOrDefault("boolean", 4);
            Assert.fail();
        } catch (PropertyConversionException pce) {
            Assert.assertEquals(pce.key, "boolean");
            Assert.assertEquals(pce.value, "false");
            Assert.assertEquals(pce.targetType, PropertyConversionException.TargetType.INTEGER);
        }
    }

    //TODO same for string, boolean and double !
}
