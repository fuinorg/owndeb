/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see http://www.gnu.org/licenses/.
 */
package org.fuin.owndeb.commons;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.fuin.utils4j.JaxbUtils.XML_PREFIX;
import static org.fuin.utils4j.JaxbUtils.marshal;
import static org.fuin.utils4j.JaxbUtils.unmarshal;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.fuin.utils4j.Utils4J;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//CHECKSTYLE:OFF for tests
public class VariableTest {

    private static final String NAME = "a";

    private static final String VALUE = "1";

    private Variable testee;

    @Before
    public void setup() {
        testee = new Variable(NAME, VALUE);
    }

    @After
    public void teardown() {
        testee = null;
    }

    @Test
    public void testConstructorNameValue() {

        // TEST
        final Variable testee = new Variable(NAME, VALUE);

        // VERIFY
        assertThat(testee.getName()).isEqualTo(NAME);
        assertThat(testee.getValue()).isEqualTo(VALUE);
        assertThat(testee.getURL()).isNull();
        assertThat(testee.getEncoding()).isNull();
        assertThat(testee.getEncodingOrDefault()).isEqualTo("utf-8");

    }

    @Test
    public void testConstructorUrl() {

        // PREPARE
        final URL url = Utils4J.url("classpath:test.properties");

        // TEST
        final Variable testee = new Variable(NAME, url);

        // VERIFY
        assertThat(testee.getName()).isEqualTo(NAME);
        assertThat(testee.getValue())
                .isEqualTo("one=1\r\ntwo=2\r\nthree=3\r\n");
        assertThat(testee.getURL()).isEqualTo(url);
        assertThat(testee.getEncoding()).isNull();
        assertThat(testee.getEncodingOrDefault()).isEqualTo("utf-8");

    }

    @Test
    public void testConstructorUrlEncoding() {

        // PREPARE
        final URL url = Utils4J.url("classpath:test.properties");

        // TEST
        final Variable testee = new Variable(NAME, url, "ISO-8859-1");

        // VERIFY
        assertThat(testee.getName()).isEqualTo(NAME);
        assertThat(testee.getValue())
                .isEqualTo("one=1\r\ntwo=2\r\nthree=3\r\n");
        assertThat(testee.getURL()).isEqualTo(url);
        assertThat(testee.getEncoding()).isEqualTo("ISO-8859-1");
        assertThat(testee.getEncodingOrDefault()).isEqualTo("ISO-8859-1");

    }

    @Test
    public void testEqualsHashCode() throws MalformedURLException {
        EqualsVerifier
                .forClass(Variable.class)
                .withPrefabValues(URL.class,
                        new URL("http://www.fuin.org/text1.txt"),
                        new URL("http://www.fuin.org/text2.txt"))
                .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS)
                .verify();
    }

    @Test
    public void testEmptyName() {
        try {
            new Variable("", VALUE);
            fail();
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo(
                    "The argument 'name' cannot be empty");
        }
    }

    @Test
    public void testNullValue() {
        try {
            new Variable(NAME, (String) null);
            fail();
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo(
                    "The argument 'value' cannot be null");
        }
    }

    @Test
    public void testEmptyEncoding() throws MalformedURLException {
        try {
            new Variable(NAME, new URL("http://www.fuin.org/test.txt"), "");
            fail();
        } catch (final IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo(
                    "The argument 'encoding' cannot be empty");
        }
    }

    @Test
    public final void testMarshalUnmarshalXML() throws Exception {

        // PREPARE
        final Variable original = new Variable(NAME, VALUE);

        // TEST
        final String xml = marshal(original, createXmlAdapter(), Variable.class);

        // VERIFY
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(XML_PREFIX
                + "<variable name=\"a\" value=\"1\"/>", xml);
        final Variable copy = unmarshal(xml, createXmlAdapter(), Variable.class);
        assertThat(copy.getName()).isEqualTo("a");
        assertThat(copy.getValue()).isEqualTo("1");
    }

    @Test
    public final void testMarshalUnmarshalEquals() throws Exception {

        // PREPARE
        final Variable original = testee;

        // TEST
        final String xml = marshal(original, createXmlAdapter(), Variable.class);

        final Variable copy = unmarshal(xml, createXmlAdapter(), Variable.class);

        // VERIFY
        assertThat(copy).isEqualTo(original);
    }

    @Test
    public final void testFile() throws MalformedURLException {

        // PREPARE
        final URL url = new File("src/test/resources/test.properties").toURI()
                .toURL();

        // TEST
        final Variable testee = new Variable(NAME, url);

        // VERIFY
        assertThat(testee.getName()).isEqualTo(NAME);
        assertThat(testee.getValue())
                .isEqualTo("one=1\r\ntwo=2\r\nthree=3\r\n");
        assertThat(testee.getURL()).isEqualTo(url);
        assertThat(testee.getEncoding()).isNull();
        assertThat(testee.getEncodingOrDefault()).isEqualTo("utf-8");

    }

    private XmlAdapter<?, ?>[] createXmlAdapter() {
        // Not necessary now - Add XML adapter if needed later on...
        return new XmlAdapter[] {};
    }

}
// CHECKSTYLE:ON
