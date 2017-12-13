# personium-ex-slack-messenger

## About

This is a [Personium](http://personium.io)'s Engine extension for sending Slack message to specified channel.

## Usage

1. Clone this repository.
2. Compile the source codes to `Ext_Slack.jar` by maven command `mvn package -DskipTests=true`.
3. Set the jar module `Ext_Slack.jar` into the Engine Extension directory in Personium application server. Default directory is `/personium/personium-engine/extensions/`.
4. Restart tomcat process.
5. Set an Engine Service which call this slack-message function.

Sample script is below.
```
function(request) {
    var token = "xoxb-xxxx";
    var defaultChannel = "random";

    var slack = new _p.extension.Slack();
    slack.setConfig(token, defaultChannel);

    slack.sendMessage("Hello, I am persoinum slack bot in default channel");
    slack.sendMessageToUser("Hello, I am persoinum slack bot: ", "${user}");
    slack.sendMessageToChannel("Hello, I am persoinum slack bot: ", "${channel}");

    // with proxy 
    // var slack = new _p.extension.Slack();
    // var proxyConf = {
    //    host: "your.proxy.host", 
    //    port: "8080",
    //    user: "proxy.user",
    //    password: "proxy.password",
    // };
    // slack.setConfig(token, defaultChannel, proxyConf);
    // slack.sendMessage("Hello, I am persoinum slack bot in random channel with proxy");

    return {
        status : 200,
        headers : {"Content-Type":"application/json"},
        body : ['Complete to send slack message!']
    };
}
```

6. Grant the `D:exec` privilege to using role and call Engine Execution API by the user who has this role.

## Test
1. If you use conf file, edit property file conf/Ext_Slack.sample.properties to set token and channel, rename and save conf/Ext_Slack.properties. And run the test.
2. If you use environments, when you start to run the test, you can set -D environments such as -Dio.personium.engine.extension.Slack.apitoken="~" -Dio.per.., .

## Use slack bot api library
### Simple-slack-api
* https://github.com/Ullink/simple-slack-api


## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.