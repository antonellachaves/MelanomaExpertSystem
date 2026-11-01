
package model;

public class PrimaryTumor {

    private final PrimaryTumorType type;
    private final Double breslowThicknessMm;
    private final Boolean ulceration;

    // Invasive melanoma with known Breslow and ulceration
    public PrimaryTumor(double breslowThicknessMm, boolean ulceration) {

        if (!Double.isFinite(breslowThicknessMm)
                || breslowThicknessMm <= 0) {
            throw new IllegalArgumentException(
                    "Breslow thickness must be greater than 0 mm for an invasive melanoma."
            );
        }

        this.type = PrimaryTumorType.INVASIVE;
        this.breslowThicknessMm = breslowThicknessMm;
        this.ulceration = ulceration;
    }


    // Invasive melanoma with potentially missing clinical data
    public PrimaryTumor(Double breslowThicknessMm, Boolean ulceration) {

        if (breslowThicknessMm != null &&
                (!Double.isFinite(breslowThicknessMm)
                        || breslowThicknessMm <= 0)) {

            throw new IllegalArgumentException(
                    "Breslow thickness must be greater than 0 mm when provided."
            );
        }

        this.type = PrimaryTumorType.INVASIVE;
        this.breslowThicknessMm = breslowThicknessMm;
        this.ulceration = ulceration;
    }


    // Special AJCC8 primary tumor categories
    private PrimaryTumor(PrimaryTumorType type) {

        if (type == null || type == PrimaryTumorType.INVASIVE) {
            throw new IllegalArgumentException(
                    "Use the Breslow constructor for invasive melanoma."
            );
        }

        this.type = type;
        this.breslowThicknessMm = null;
        this.ulceration = null;
    }

    public static PrimaryTumor inSitu() {
        return new PrimaryTumor(PrimaryTumorType.IN_SITU);
    }

    public static PrimaryTumor noPrimaryTumor() {
        return new PrimaryTumor(PrimaryTumorType.NO_PRIMARY_TUMOR);
    }

    public static PrimaryTumor cannotAssess() {
        return new PrimaryTumor(PrimaryTumorType.CANNOT_ASSESS);
    }

    public PrimaryTumorType getType() {
        return type;
    }

    public Double getBreslowThicknessMm() {
        return breslowThicknessMm;
    }

    public Boolean getUlceration() {
        return ulceration;
    }

    public Boolean isUlceration() {
        return ulceration;
    }
}
