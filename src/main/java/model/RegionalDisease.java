
package model;

public class RegionalDisease {

    private Integer involvedRegionalNodes;
    private Boolean clinicallyDetectedNode;
    private Boolean inTransitMetastasis;
    private Boolean satelliteMetastasis;
    private Boolean microsatelliteMetastasis;
    private Boolean mattedNodes;

    public RegionalDisease() {
        // Los campos permanecen en null si no se introducen.
    }

    public RegionalDisease(
            Integer involvedRegionalNodes,
            Boolean clinicallyDetectedNode,
            Boolean inTransitMetastasis,
            Boolean satelliteMetastasis,
            Boolean microsatelliteMetastasis,
            Boolean mattedNodes) {

        setInvolvedRegionalNodes(involvedRegionalNodes);
        this.clinicallyDetectedNode = clinicallyDetectedNode;
        this.inTransitMetastasis = inTransitMetastasis;
        this.satelliteMetastasis = satelliteMetastasis;
        this.microsatelliteMetastasis = microsatelliteMetastasis;
        this.mattedNodes = mattedNodes;
    }

    public Integer getInvolvedRegionalNodes() {
        return involvedRegionalNodes;
    }

    public void setInvolvedRegionalNodes(Integer nodes) {
        if (nodes != null && nodes < 0) {
            throw new IllegalArgumentException(
                    "Number of involved regional nodes cannot be negative.");
        }
        this.involvedRegionalNodes = nodes;
    }

    public Boolean getClinicallyDetectedNode() {
        return clinicallyDetectedNode;
    }

    public void setClinicallyDetectedNode(Boolean value) {
        this.clinicallyDetectedNode = value;
    }

    public Boolean getInTransitMetastasis() {
        return inTransitMetastasis;
    }

    public void setInTransitMetastasis(Boolean value) {
        this.inTransitMetastasis = value;
    }

    public Boolean getSatelliteMetastasis() {
        return satelliteMetastasis;
    }

    public void setSatelliteMetastasis(Boolean value) {
        this.satelliteMetastasis = value;
    }

    public Boolean getMicrosatelliteMetastasis() {
        return microsatelliteMetastasis;
    }

    public void setMicrosatelliteMetastasis(Boolean value) {
        this.microsatelliteMetastasis = value;
    }

    public Boolean getMattedNodes() {
        return mattedNodes;
    }

    public void setMattedNodes(Boolean value) {
        this.mattedNodes = value;
    }

    public Boolean hasLocoregionalMetastasis() {
        if (Boolean.TRUE.equals(inTransitMetastasis)
                || Boolean.TRUE.equals(satelliteMetastasis)
                || Boolean.TRUE.equals(microsatelliteMetastasis)) {
            return true;
        }

        if (Boolean.FALSE.equals(inTransitMetastasis)
                && Boolean.FALSE.equals(satelliteMetastasis)
                && Boolean.FALSE.equals(microsatelliteMetastasis)) {
            return false;
        }

        return null;
    }
}
