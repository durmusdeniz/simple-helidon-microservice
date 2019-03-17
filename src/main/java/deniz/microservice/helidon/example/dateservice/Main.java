/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
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

package deniz.microservice.helidon.example.dateservice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;

import io.helidon.config.Config;
import io.helidon.health.HealthSupport;
import io.helidon.health.checks.HealthChecks;
import io.helidon.media.jsonp.server.JsonSupport;
import io.helidon.metrics.MetricsSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;

/**
 * Simple Hello World rest application.
 */
public final class Main {

    /**
     * Cannot be instantiated.
     */
    private Main() { }

    /**
     * Application main entry point.
     * @param args command line arguments.
     * @throws IOException if there are problems reading logging properties
     */
    public static void main(final String[] args) throws Exception {


        List<String> zoneList = Arrays.asList(TimeZone.getAvailableIDs());

        WebServer webServer = WebServer.create(
                Routing.builder()
                        .get("/getzonelist", (req, res) ->{
                            res.send(zoneList);
                        })
                        .get("/timeat", (req, res) ->{
                            String responseBody = "No such zone";
                            Optional<String> zoneId = req.queryParams().first("zone");
                            if(zoneId.isPresent()){
                                if(zoneList.contains(zoneId.get())){
                                    responseBody = LocalDateTime.now(ZoneId.of(zoneId.get()))+"";
                                }else{
                                 responseBody = LocalDateTime.now()+"";
                                }
                            }
                            res.send(responseBody);
                        })
                        .build()
        ).start()
                .toCompletableFuture().get(10, TimeUnit.SECONDS);


        System.out.println("Date Service is Up");



    }




}
