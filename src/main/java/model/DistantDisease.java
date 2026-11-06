
package model;

public class DistantDisease {

    private final Boolean distantMetastasis;
    private final Boolean distantSkinSoftTissueNonregionalNode;
    private final Boolean lungMetastasis;
    private final Boolean nonCnsVisceralMetastasis;
    private final Boolean cnsMetastasis;
    private final LdhStatus ldhStatus;

    public DistantDisease(
            Boolean distantMetastasis,
            Boolean distantSkinSoftTissueNonregionalNode,
            Boolean lungMetastasis,
            Boolean nonCnsVisceralMetastasis,
            Boolean cnsMetastasis,
            LdhStatus ldhStatus) {

        if (Boolean.FALSE.equals(distantMetastasis)
                && (Boolean.TRUE.equals(distantSkinSoftTissueNonregionalNode)
                || Boolean.TRUE.equals(lungMetastasis)
                || Boolean.TRUE.equals(nonCnsVisceralMetastasis)
                || Boolean.TRUE.equals(cnsMetastasis))) {

            throw new IllegalArgumentException(
                    "Contradictory data: metastatic site present but distant metastasis is false.");
        }

        if (Boolean.TRUE.equals(distantSkinSoftTissueNonregionalNode)
                || Boolean.TRUE.equals(lungMetastasis)
                || Boolean.TRUE.equals(nonCnsVisceralMetastasis)
                || Boolean.TRUE.equals(cnsMetastasis)) {

            if (distantMetastasis == null) {
                throw new IllegalArgumentException(
                        "Distant metastasis status must be confirmed when a metastatic site is present.");
            }
        }

        this.distantMetastasis = distantMetastasis;
        this.distantSkinSoftTissueNonregionalNode =
                distantSkinSoftTissueNonregionalNode;
        this.lungMetastasis = lungMetastasis;
        this.nonCnsVisceralMetastasis = nonCnsVisceralMetastasis;
        this.cnsMetastasis = cnsMetastasis;
        this.ldhStatus = ldhStatus;
    }

    public Boolean getDistantMetastasis() {
        return distantMetastasis;
    }

    public Boolean getDistantSkinSoftTissueNonregionalNode() {
        return distantSkinSoftTissueNonregionalNode;
    }

    public Boolean getLungMetastasis() {
        return lungMetastasis;
    }

    public Boolean getNonCnsVisceralMetastasis() {
        return nonCnsVisceralMetastasis;
    }

    public Boolean getCnsMetastasis() {
        return cnsMetastasis;
    }

    public LdhStatus getLdhStatus() {
        return ldhStatus;
    }
}
