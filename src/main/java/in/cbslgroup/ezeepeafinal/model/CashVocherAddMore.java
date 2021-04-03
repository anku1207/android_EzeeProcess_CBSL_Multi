package in.cbslgroup.ezeepeafinal.model;

public class CashVocherAddMore {

    private String Rupee;
    private String Paisa;
    private String Description;

    public CashVocherAddMore(String rupee, String paisa, String description) {
        Rupee = rupee;
        Paisa = paisa;
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


    public String getRupee() {
        return Rupee;
    }

    public void setRupee(String rupee) {
        Rupee = rupee;
    }

    public String getPaisa() {
        return Paisa;
    }

    public void setPaisa(String paisa) {
        Paisa = paisa;
    }

}

