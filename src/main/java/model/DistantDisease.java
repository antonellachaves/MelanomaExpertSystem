package model;

public class DistantDisease {

    private boolean distantMetastasis;
    private boolean distantSkinSoftTissueNonregionalNode;
    private boolean lungMetastasis;
    private boolean nonCnsVisceralMetastasis;
    private boolean cnsMetastasis;
    private LdhStatus ldhStatus;

    public DistantDisease() {
    }

    public DistantDisease(boolean distantMetastasis,
                          boolean distantSkinSoftTissueNonregionalNode,
                          boolean lungMetastasis,
                          boolean nonCnsVisceralMetastasis,
                          boolean cnsMetastasis,
                          LdhStatus ldhStatus) {

        this.distantMetastasis = distantMetastasis;
        this.distantSkinSoftTissueNonregionalNode =
                distantSkinSoftTissueNonregionalNode;
        this.lungMetastasis = lungMetastasis;
        this.nonCnsVisceralMetastasis = nonCnsVisceralMetastasis;
        this.cnsMetastasis = cnsMetastasis;
        this.ldhStatus = ldhStatus;
    }

    public boolean isDistantMetastasis() {
        return distantMetastasis;
    }

    public void setDistantMetastasis(boolean distantMetastasis) {
        this.distantMetastasis = distantMetastasis;
    }

    public boolean isDistantSkinSoftTissueNonregionalNode() {
        return distantSkinSoftTissueNonregionalNode;
    }

    public void setDistantSkinSoftTissueNonregionalNode(
            boolean distantSkinSoftTissueNonregionalNode) {
        this.distantSkinSoftTissueNonregionalNode =
                distantSkinSoftTissueNonregionalNode;
    }

    public boolean isLungMetastasis() {
        return lungMetastasis;
    }

    public void setLungMetastasis(boolean lungMetastasis) {
        this.lungMetastasis = lungMetastasis;
    }

    public boolean isNonCnsVisceralMetastasis() {
        return nonCnsVisceralMetastasis;
    }

    public void setNonCnsVisceralMetastasis(
            boolean nonCnsVisceralMetastasis) {
        this.nonCnsVisceralMetastasis = nonCnsVisceralMetastasis;
    }

    public boolean isCnsMetastasis() {
        return cnsMetastasis;
    }

    public void setCnsMetastasis(boolean cnsMetastasis) {
        this.cnsMetastasis = cnsMetastasis;
    }

    public LdhStatus getLdhStatus() {
        return ldhStatus;
    }

    public void setLdhStatus(LdhStatus ldhStatus) {
        this.ldhStatus = ldhStatus;
    }
}