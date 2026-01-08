package biblioteca;

import java.time.LocalDate;

public class Socio {

    private int tessera;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private char sesso;
    private String email;
    private String telefono;

    public Socio(int tessera, String nome, String cognome, LocalDate dataNascita, char sesso, String email, String telefono) {
        this.tessera = tessera;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.sesso = sesso;
        this.email = email;
        this.telefono = telefono;
    }

    public int getTessera() {
        return tessera;
    }

    public void setTessera(int tessera) {
        this.tessera = tessera;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public char getSesso() {
        return sesso;
    }

    public void setSesso(char sesso) {
        this.sesso = sesso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
