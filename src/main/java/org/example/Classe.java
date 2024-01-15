package org.example;

import java.util.*;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Classe {
    int numero;
    String sezione;
    String aula;
    @JacksonXmlElementWrapper(localName = "alunni")
    @JacksonXmlProperty(localName = "alunno")
    ArrayList<Alunno> alunni;

    public Classe() {

    }

    public Classe(int numero, String sezione, String aula, ArrayList<Alunno> alunni) {
        this.numero = numero;
        this.sezione = sezione;
        this.aula = aula;
        this.alunni = alunni;
    }

    public int getNumero() {
        return numero;
    }

    public String getSezione() {
        return sezione;
    }

    public String getAula() {
        return aula;
    }

    public ArrayList<Alunno> getAlunni() {
        return alunni;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setSezione(String sezione) {
        this.sezione = sezione;
    }


    public void setAula(String aula) {
        this.aula = aula;
    }

    public void setAlunni(ArrayList<Alunno> alunni) {
        this.alunni = alunni;
    }

    public void addAlunno(Alunno a) {
        this.alunni.add(a);
    }
}
