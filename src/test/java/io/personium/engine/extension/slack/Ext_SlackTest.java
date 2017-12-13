/**
 * personium.io
 * Copyright 2017 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.personium.engine.extension.slack;
import org.mozilla.javascript.NativeObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.*;

public class Ext_SlackTest {
    // You must configure these settings using '-D' arguments or conf/Ext_Slack.properties.

    private static final String EXT_SLACK_API_TOKEN = "io.personium.engine.extension.Slack.apitoken";
    private static final String EXT_SLACK_CHANNEL = "io.personium.engine.extension.Slack.channel";
    private static final String EXT_SLACK_USER = "io.personium.engine.extension.Slack.user";
    private static final String EXT_PROXY_HOST = "io.personium.engine.extension.Slack.proxy.host";
    private static final String EXT_PROXY_PORT = "io.personium.engine.extension.Slack.proxy.port";
    private static final String EXT_PROXY_USER = "io.personium.engine.extension.Slack.proxy.user";
    private static final String EXT_PROXY_PASSWORD = "io.personium.engine.extension.Slack.proxy.password";

    private String slackAPItoken = System.getProperties().getProperty(EXT_SLACK_API_TOKEN);
    private String defaultChannel = System.getProperties().getProperty(EXT_SLACK_CHANNEL);
    private String slackUser = System.getProperties().getProperty(EXT_SLACK_USER);
    private String proxyHost = System.getProperties().getProperty(EXT_PROXY_HOST);
    private String proxyPort = System.getProperties().getProperty(EXT_PROXY_PORT);
    private String proxyUser = System.getProperties().getProperty(EXT_PROXY_USER);
    private String proxyPassword = System.getProperties().getProperty(EXT_PROXY_PASSWORD);

    /**
     * get properties thorough config file
     */
    private void getProperties() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("conf/Ext_Slack.properties");
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            slackAPItoken = prop.getProperty(EXT_SLACK_API_TOKEN);
            defaultChannel = prop.getProperty(EXT_SLACK_CHANNEL);
            slackUser = prop.getProperty(EXT_SLACK_USER);
            proxyHost = prop.getProperty(EXT_PROXY_HOST);
            proxyPort = prop.getProperty(EXT_PROXY_PORT);
            proxyUser = prop.getProperty(EXT_PROXY_USER);
            proxyPassword = prop.getProperty(EXT_PROXY_PASSWORD);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Ext_Slack slack = new Ext_Slack();

    @org.junit.Before
    public void setUp() throws Exception {
        if(slackAPItoken == null) {
            getProperties();
        }
        if(proxyHost != null) {
            NativeObject proxyInfo = new NativeObject();
            proxyInfo.put("host", proxyInfo, proxyHost);
            proxyInfo.put("port", proxyInfo, proxyPort);
            proxyInfo.put("user", proxyInfo, proxyUser);
            proxyInfo.put("password", proxyInfo, proxyPassword);
            slack.setConfig(slackAPItoken, defaultChannel, proxyInfo);
        }else {
            slack.setConfig(slackAPItoken, defaultChannel, null);
        }
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void message() throws Exception {
        try {
            slack.sendMessage("Test message to default channel");
            assertTrue(true);
        } catch(Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @org.junit.Test
    public void messageToChannel() throws Exception {
        try {
            slack.sendMessageToChannel("Test message to channel", "general");
            assertTrue(true);
        } catch(Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @org.junit.Test
    public void messageToUser() throws Exception {
        try {
            slack.sendMessageToUser("Test message to user", slackUser);
            assertTrue(true);
        } catch(Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}