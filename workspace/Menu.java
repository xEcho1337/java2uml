package biblioteca;

import java.time.LocalDate;
import java.util.Scanner;

public class Menu {
    
    private final Scanner scanner = new Scanner(System.in);
    private final Socio[] soci = new Socio[500];
    private final Libro[] libri = new Libro[1000];
    private final Prestito[] prestiti = new Prestito[1000];
    
    public static void main(String[] args) {
        new Menu().start();
    }
    
    private void start() {
        int scelta;
        
        do {
            System.out.println("\n--- BIBLIOTECA ---");
            System.out.println("1. Inserisci nuovo socio");
            System.out.println("2. Inserisci nuovo libro");
            System.out.println("3. Inserisci nuovo prestito");
            System.out.println("4. Registra riconsegna");
            System.out.println("5. Elimina prestiti scaduti e restituiti (>3 anni)");
            System.out.println("6. Visualizza prestiti scaduti (>1 settimana)");
            System.out.println("7. Visualizza tutti i prestiti");
            System.out.println("0. Esci");
            
            scelta = scanner.nextInt();
            scanner.nextLine();
            
            switch (scelta) {
                case 1 -> inserisciSocio();
                case 2 -> inserisciLibro();
                case 3 -> inserisciPrestito();
                case 4 -> registraRiconsegna();
                case 5 -> eliminaPrestitiScaduti();
                case 6 -> visualizzaPrestitiScaduti();
                case 7 -> visualizzaPrestiti();
            }
            
        } while (scelta != 0);
    }
    
    private int trovaPrimoVuoto(Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) return i;
        }
        
        return -1;
    }
    
    private void inserisciSocio() {
        int idx = trovaPrimoVuoto(soci);
        if (idx == -1) {
            System.out.println("La lista dei soci è al completo!");
            return;
        }
        
        System.out.print("Tessera: ");
        int tessera = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();
        
        System.out.print("Anno nascita: ");
        int a = scanner.nextInt();
        System.out.print("Mese: ");
        int m = scanner.nextInt();
        System.out.print("Giorno: ");
        int g = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Sesso (M/F): ");
        char sesso = scanner.next().charAt(0);
        scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();
        
        soci[idx] = new Socio(tessera, nome, cognome, LocalDate.of(a, m, g), sesso, email, telefono);
        System.out.println("Socio inserito.");
    }
    
    private void inserisciLibro() {
        int idx = trovaPrimoVuoto(libri);
        if (idx == -1) {
            System.out.println("La lista dei libri è al completo!");
            return;
        }
        
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        
        System.out.print("Progressivo: ");
        int prog = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Numero autori (1-5): ");
        int n = scanner.nextInt();
        scanner.nextLine();
        
        String[] autori = new String[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Autore " + (i + 1) + ": ");
            autori[i] = scanner.nextLine();
        }
        
        System.out.print("Titolo: ");
        String titolo = scanner.nextLine();
        
        System.out.print("Sala: ");
        char sala = scanner.next().charAt(0);
        
        System.out.print("Scaffale: ");
        int scaffale = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Ripiano: ");
        int ripiano = scanner.nextInt();
        scanner.nextLine();
        
        libri[idx] = new Libro(isbn, prog, autori, titolo, sala, scaffale, ripiano);
        System.out.println("Libro inserito.");
    }
    
    private void inserisciPrestito() {
        int idx = trovaPrimoVuoto(prestiti);
        if (idx == -1) {
            System.out.println("La lista dei prestiti è al completo!");
            return;
        }
        
        System.out.print("Tessera socio: ");
        int tessera = scanner.nextInt();
        scanner.nextLine();
        
        Socio socio = null;
        for (Socio s : soci) {
            if (s != null && s.getTessera() == tessera) socio = s;
        }
        
        if (socio == null) {
            System.out.println("Socio non trovato.");
            return;
        }
        
        System.out.print("Numero libri: ");
        int n = scanner.nextInt();
        scanner.nextLine();
        
        Libro[] lp = new Libro[n];
        for (int i = 0; i < n; i++) {
            System.out.print("ISBN libro: ");
            String isbn = scanner.nextLine();
            
            for (Libro l : libri) {
                if (l != null && l.getIsbn().equals(isbn)) lp[i] = l;
            }
        }
        
        prestiti[idx] = new Prestito(LocalDate.now(), lp, socio, false);
        System.out.println("Prestito registrato.");
    }
    
    private void registraRiconsegna() {
        System.out.print("Inserisci la tessera del socio di cui restituire il prestito: ");
        int tessera = scanner.nextInt();
        scanner.nextLine();
        
        for (Prestito p : prestiti) {
            if (p == null || p.getSocio().getTessera() != tessera || p.isRiconsegna()) continue;
            
            p.setRiconsegna(true);
            System.out.println("Riconsegna registrata.");
            return;
        }
    }
    
    private void eliminaPrestitiScaduti() {
        LocalDate limite = LocalDate.now().minusYears(3);
        for (int i = 0; i < prestiti.length; i++) {
            Prestito p = prestiti[i];
            if (p != null && p.isRiconsegna() && p.getDataInizio().isBefore(limite)) {
                prestiti[i] = null;
            }
        }
    }
    
    private void visualizzaPrestitiScaduti() {
        LocalDate limite = LocalDate.now().minusWeeks(1);
        for (Prestito p : prestiti) {
            if (p != null && !p.isRiconsegna() && p.getDataInizio().isBefore(limite)) {
                Socio s = p.getSocio();
                System.out.println(s.getNome() + " " + s.getCognome() + " - " + s.getEmail());
            }
        }
    }
    
    private void visualizzaPrestiti() {
        for (Prestito p : prestiti) {
            if (p != null) {
                System.out.println(
                    p.getDataInizio() + " - " +
                        p.getSocio().getNome() + " " +
                        p.getSocio().getCognome()
                );
            }
        }
    }
}
