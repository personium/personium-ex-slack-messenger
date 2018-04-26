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

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import io.personium.engine.extension.support.AbstractExtensionScriptableObject;
import io.personium.engine.extension.support.ExtensionLogger;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;

@SuppressWarnings("serial")
public class Ext_Slack extends AbstractExtensionScriptableObject {

    static Logger log = LoggerFactory.getLogger(Ext_Slack.class);

    private String SlackAPItoken = null;
    private String DefaultChannel = null;
    private String ProxyHost = null;
    private String ProxyPort = null;
    private String ProxyUser = null;
    private String ProxyPassword = null;

    /**
     * Public name to JavaScript.
     */
    @Override
    public String getClassName() {
        return "Slack";
    }

    /**
     * constructor.
     */
    @JSConstructor
    public Ext_Slack() {
        ExtensionLogger logger = new ExtensionLogger(this.getClass());
        setLogger(this.getClass(), logger);
    }

    /**
     *
     * @return
     */
    private SlackSession getSession() {
        SlackSession session = null;

        try {
            if (ProxyHost != null && ProxyUser != null) {
                int port = Integer.parseInt(ProxyPort);
                session = SlackSessionFactory.getSlackSessionBuilder(SlackAPItoken)
                        .withProxy(Proxy.Type.HTTP, ProxyHost, port, ProxyUser, ProxyPassword)
                        .build();
            } else if (ProxyHost != null && ProxyUser == null) {
                int port = Integer.parseInt(ProxyPort);
                session = SlackSessionFactory.getSlackSessionBuilder(SlackAPItoken)
                        .withProxy(Proxy.Type.HTTP, ProxyHost, port)
                        .build();
            } else {
                session = SlackSessionFactory.createWebSocketSlackSession(SlackAPItoken);
            }
        } catch (Exception e) {
            this.getLogger().error("Something happened in getSession: " + e);
        }

        return session;
    }

    /**
     *
     * @param slackAPItoken
     * @param defaultChannel
     * @param proxyInfo
     * @throws IOException
     */
    @JSFunction
    public void setConfig(String slackAPItoken, String defaultChannel, NativeObject proxyInfo) throws IOException {
        if (slackAPItoken != null) {
            SlackAPItoken = slackAPItoken;
        }
        if (defaultChannel == null) {
            DefaultChannel = "general";
        } else {
            DefaultChannel = defaultChannel;
        }
        if (proxyInfo != null) {
            for (Map.Entry<Object, Object> e : proxyInfo.entrySet()) {
                String key = e.getKey().toString();
                if (key.equals("host")) {
                    ProxyHost = e.getValue().toString();
                } else if (key.equals("port")) {
                    ProxyPort = e.getValue().toString();
                } else if (key.equals("user")) {
                    ProxyUser = e.getValue().toString();
                } else if (key.equals("password")) {
                    ProxyPassword = e.getValue().toString();
                }
            }
        }
    }

    /**
     *
     * @param message
     * @throws IOException
     */
    @JSFunction
    public void sendMessage(String message) throws IOException {


        try {
            SlackSession session = getSession();

            session.connect();

            SlackChannel channel = session.findChannelByName(DefaultChannel);
            session.sendMessage(channel, message);

        } catch (Exception e) {
            this.getLogger().error("Something happened in sendMessage. " + e);

        }

    }

    /**
     *
     * @param message
     * @param specifiedChannel
     * @throws IOException
     */
    @JSFunction
    public void sendMessageToChannel(String message, String specifiedChannel) throws IOException {
        try {
            SlackSession session = getSession();

            session.connect();

            SlackChannel channel = session.findChannelByName(specifiedChannel);
            session.sendMessage(channel, message);

        } catch (Exception e) {
            this.getLogger().error("Something happened in sendMessageToChannel. " + e);

        }
    }

    /**
     *
     * @param message
     * @param userName
     * @throws IOException
     */
    @JSFunction
    public void sendMessageToUser(String message, String userName) throws IOException {

        try {
            SlackSession session = getSession();

            session.connect();

            SlackUser user = session.findUserByUserName(userName);
            session.sendMessageToUser(user, message, null);

        } catch (Exception e) {
            this.getLogger().error("Something happened in sendMessageToUser. " + e);

        }

    }


}
