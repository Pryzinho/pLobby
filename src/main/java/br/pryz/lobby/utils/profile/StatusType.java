package br.pryz.lobby.utils.profile;

public enum StatusType {
    DISPONIVEL("&aDisponivel"),
    OCUPADO("&cOcupado"),
    AUSENTE("&6Ausente"),
    OFFLINE("&8Offline");

   private String txt;

    StatusType(String txt){
        this.txt = txt;
    }
}
