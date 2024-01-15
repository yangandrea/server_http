package org.example;
import java.util.*;

public class Alunno {
    String nome;
    String cognome;
    Date dataNascita;

    public Alunno() {
    }

    public Alunno(String nome, String cognome, Date dataNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome=cognome;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita=dataNascita;
    }
}

