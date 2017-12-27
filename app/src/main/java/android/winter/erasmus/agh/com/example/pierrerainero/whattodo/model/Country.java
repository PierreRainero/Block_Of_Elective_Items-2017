package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model;

/**
 * Created by PierreRainero on 27/12/2017.
 */

public enum Country {
    CZECH_REPUBLIC ("Czech republic", "cz"),
    FRANCE("France", "fr"),
    GERMANY("Germany", "ger"),
    POLAND("Poland", "pl"),
    SLOVAKIA("Slovakia", "sk"),
    SPAIN("Spain", "sp"),
    UNITED_KINGDOM("United Kingdom", "uk"),
    UKRAINE("Ukraine", "ukr");

    private String formatedName;
    private String code;

    Country(String formatedName, String code){
        this.formatedName = formatedName;
        this.code = code;
    }

    public String getFormatedName(){
        return formatedName;
    }

    public String getCode(){
        return code;
    }

    /**
     * Allow to get an enumeration object according to a formated string
     * @param value formated string to compare
     * @return anumration object or null (no equivalence)
     */
    public static Country getEnumOf(String value){
        for(Country tempo : Country.values()){
            if(tempo.getFormatedName().equals(value)){
                return tempo;
            }
        }
        return null;
    }
}

