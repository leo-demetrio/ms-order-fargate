package com.myorg;


import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.rds.*;
import software.constructs.Construct;

import java.util.Collections;


public class AwsMsorderRds extends Stack {

    public AwsMsorderRds(final Construct scope, final String id, final Vpc vpc) {
        this(scope, id, null, vpc);
    }

    public AwsMsorderRds(final Construct scope, final String id, final StackProps props, final Vpc vpc) {
        super(scope, id, props);


        CfnParameter password = CfnParameter.Builder.create ( this,  "password")
                .type("String")
                .description("Password of database orders-ms")
                .build();

        ISecurityGroup iSecurityGroup = SecurityGroup.fromSecurityGroupId(this, id, vpc.getVpcDefaultSecurityGroup());
        iSecurityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(3306));

        DatabaseInstance database = DatabaseInstance.Builder
                .create(this, "Rds-order")
                .instanceIdentifier("aws-order-db")
                .engine(DatabaseInstanceEngine.mysql(MySqlInstanceEngineProps.builder()
                        .version(MysqlEngineVersion.VER_8_0)
                        .build()))
                .vpc(vpc)
                .credentials(Credentials.fromUsername("admin",
                CredentialsFromUsernameOptions.builder()
                        .password(SecretValue.unsafePlainText(password.getValueAsString()))
                        .build()))
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .multiAz(false)
                .allocatedStorage(10)
                .securityGroups(Collections.singletonList(iSecurityGroup))
                .vpcSubnets(SubnetSelection.builder()
                        .subnets(vpc.getPrivateSubnets())
                        .build())
                .build();

        CfnOutput.Builder.create(this,"order-db-endpoint")
                .exportName("order-db-endpoint")
                .value(database.getDbInstanceEndpointAddress())
                .build();

        CfnOutput.Builder.create(this, "order-db-senha")
                .exportName("order-db-senha")
                .value(password.getValueAsString())
                .build();

    }

}
