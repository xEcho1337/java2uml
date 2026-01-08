package biblioteca;

public class Libro {

    private String isbn;
    private int progressivo;
    private String[] autori;
    private String titolo;
    private char sala;
    private int scaffale;
    private int ripiano;

    public Libro(String isbn, int progressivo, String[] autori, String titolo, char sala, int scaffale, int ripiano) {
        this.isbn = isbn;
        this.progressivo = progressivo;
        this.autori = autori;
        this.titolo = titolo;
        this.sala = sala;
        this.scaffale = scaffale;
        this.ripiano = ripiano;
        
        if (autori.length == 0) {
            throw new IllegalArgumentException("Il libro non può avere 0 o meno autori!");
        }
        
        if (autori.length > 5) {
            throw new IllegalArgumentException("Il libro non può avere più di 5 autori!");
        }
        
        if (scaffale < 1 || scaffale > 40) {
            throw new IllegalArgumentException("Lo scaffale deve essere compreso tra 1 e 40!");
        }
        
        if (ripiano < 1 || ripiano > 9) {
            throw new IllegalArgumentException("Il ripiano deve essere compreso tra 1 e 9!");
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(int progressivo) {
        this.progressivo = progressivo;
    }

    public String[] getAutori() {
        return autori;
    }

    public void setAutori(String[] autori) {
        this.autori = autori;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public char getSala() {
        return sala;
    }

    public void setSala(char sala) {
        this.sala = sala;
    }

    public int getScaffale() {
        return scaffale;
    }

    public void setScaffale(int scaffale) {
        this.scaffale = scaffale;
    }

    public int getRipiano() {
        return ripiano;
    }

    public void setRipiano(int ripiano) {
        this.ripiano = ripiano;
    }
}
