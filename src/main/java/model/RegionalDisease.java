package model;

public class RegionalDisease {

    private int involvedRegionalNodes;
    private boolean clinicallyDetectedNode;
    private boolean inTransitMetastasis;
    private boolean satelliteMetastasis;
    private boolean microsatelliteMetastasis;
    private boolean mattedNodes;

    public RegionalDisease() {
    }

    public RegionalDisease(int involvedRegionalNodes,
                           boolean clinicallyDetectedNode,
                           boolean inTransitMetastasis,
                           boolean satelliteMetastasis,
                           boolean microsatelliteMetastasis,
                           boolean mattedNodes) {

        this.involvedRegionalNodes = involvedRegionalNodes;
        this.clinicallyDetectedNode = clinicallyDetectedNode;
        this.inTransitMetastasis = inTransitMetastasis;
        this.satelliteMetastasis = satelliteMetastasis;
        this.microsatelliteMetastasis = microsatelliteMetastasis;
        this.mattedNodes = mattedNodes;
    }

    public int getInvolvedRegionalNodes() {
        return involvedRegionalNodes;
    }

    public void setInvolvedRegionalNodes(int involvedRegionalNodes) {
        this.involvedRegionalNodes = involvedRegionalNodes;
    }

    public boolean isClinicallyDetectedNode() {
        return clinicallyDetectedNode;
    }

    public void setClinicallyDetectedNode(boolean clinicallyDetectedNode) {
        this.clinicallyDetectedNode = clinicallyDetectedNode;
    }

    public boolean isInTransitMetastasis() {
        return inTransitMetastasis;
    }

    public void setInTransitMetastasis(boolean inTransitMetastasis) {
        this.inTransitMetastasis = inTransitMetastasis;
    }

    public boolean isSatelliteMetastasis() {
        return satelliteMetastasis;
    }

    public void setSatelliteMetastasis(boolean satelliteMetastasis) {
        this.satelliteMetastasis = satelliteMetastasis;
    }

    public boolean isMicrosatelliteMetastasis() {
        return microsatelliteMetastasis;
    }

    public void setMicrosatelliteMetastasis(boolean microsatelliteMetastasis) {
        this.microsatelliteMetastasis = microsatelliteMetastasis;
    }

    public boolean isMattedNodes() {
        return mattedNodes;
    }

    public void setMattedNodes(boolean mattedNodes) {
        this.mattedNodes = mattedNodes;
    }

    public boolean hasLocoregionalMetastasis() {
        return inTransitMetastasis
                || satelliteMetastasis
                || microsatelliteMetastasis;
    }
}
