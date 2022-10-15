package com.myorg;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;


public class AwsMsorderVpc extends Stack {

    private Vpc vpc;
    public AwsMsorderVpc(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AwsMsorderVpc(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        vpc = Vpc.Builder.create(this, "MsorderVpc")
                .maxAzs(3)
                .build();
    }

    public Vpc getVpc() {
        return vpc;
    }
}
