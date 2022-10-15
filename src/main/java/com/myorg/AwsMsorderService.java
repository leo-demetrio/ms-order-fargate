package com.myorg;

import software.amazon.awscdk.Fn;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.constructs.Construct;

import java.util.HashMap;
import java.util.Map;


public class AwsMsorderService extends Stack {


    public AwsMsorderService(final Construct scope, final String id, final Cluster cluster) {
        this(scope, id, null, cluster);
    }

    public AwsMsorderService(final Construct scope, final String id, final StackProps props, final Cluster cluster) {
        super(scope, id, props);

        Map<String, String> authenticate = new HashMap<>();
        authenticate.put("SPRING_DATASOURCE_URL", "jdbc:mysql://"
                + Fn.importValue("order-db-endpoint")
                + ":3306/odb_order?createDatabaseIfNotExist=true");

        authenticate.put("SPRING_DATASOURCE_USERNAME", "admin");
        authenticate.put("SPRING_DATASOURCE_PASSWORD", Fn.importValue("order-db-senha"));

        ApplicationLoadBalancedFargateService.Builder.create(this, "MsorderService")
                .serviceName("service_msorder")
                .cluster(cluster)
                .cpu(512)
                .desiredCount(1)
                .listenerPort(8080)
                .assignPublicIp(true)
                .taskImageOptions(
                        ApplicationLoadBalancedTaskImageOptions.builder()
                                .image(ContainerImage.fromRegistry("leopjockerdocker/msorder:1.0"))
                                .containerPort(8080)
                                .containerName("app_msorder")
                                .environment(authenticate)
                                .build())
                .memoryLimitMiB(1024)
                .publicLoadBalancer(true)
                .build();


    }


}
