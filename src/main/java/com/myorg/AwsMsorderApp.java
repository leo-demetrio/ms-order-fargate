package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class AwsMsorderApp {
    public static void main(final String[] args) {
        App app = new App();

        AwsMsorderVpc awsMsorderVpc = new AwsMsorderVpc(app, "Vpc");

        AwsMsorderCluster awsMsorderCluster = new AwsMsorderCluster(app, "Cluster", awsMsorderVpc.getVpc());
        awsMsorderCluster.addDependency(awsMsorderVpc);

        AwsMsorderRds awsMsorderRds = new AwsMsorderRds(app, "MsorderRds", awsMsorderVpc.getVpc());
        awsMsorderRds.addDependency(awsMsorderVpc);

        AwsMsorderService awsMsorderService = new AwsMsorderService(app, "Service", awsMsorderCluster.getCluster());
        awsMsorderService.addDependency(awsMsorderCluster);
        awsMsorderService.addDependency(awsMsorderRds);

        app.synth();
    }
}

