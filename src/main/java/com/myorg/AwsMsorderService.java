package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.constructs.Construct;


public class AwsMsorderService extends Stack {


    public AwsMsorderService(final Construct scope, final String id, final Cluster cluster) {
        this(scope, id, null, cluster);
    }

    public AwsMsorderService(final Construct scope, final String id, final StackProps props, final Cluster cluster) {
        super(scope, id, props);

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
                                .build())
                .memoryLimitMiB(1024)
                .publicLoadBalancer(true)
                .build();


    }


}
