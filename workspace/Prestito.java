package biblioteca;

import java.time.LocalDate;

public class Prestito {

    private LocalDate dataInizio;
    private Libro[] libriPrestati;
    private Socio socio;
    private boolean riconsegna;

    public Prestito(LocalDate dataInizio, Libro[] libriPrestati, Socio socio, boolean riconsegna) {
        this.dataInizio = dataInizio;
        this.libriPrestati = libriPrestati;
        this.socio = socio;
        this.riconsegna = riconsegna;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Libro[] getLibriPrestati() {
        return libriPrestati;
    }

    public void setLibriPrestati(Libro[] libriPrestati) {
        this.libriPrestati = libriPrestati;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public boolean isRiconsegna() {
        return riconsegna;
    }

    public void setRiconsegna(boolean riconsegna) {
        this.riconsegna = riconsegna;
    }
}
