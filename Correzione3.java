/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package correzione3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author ame-9
 */
public class Correzione3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Docente docente0 = new Docente("Magnifico","Rettore");
        Docente docente = new Docente("Enrico","Vicario");
        Docente docente2 = new Docente("Paolo","Frasconi");
        
        
        List<Tecnico> tecnici = new ArrayList<>();
        tecnici.add(new Tecnico("Mauro","Angioni"));
        tecnici.add(new Tecnico("Cristiano","Nesta"));
        
        List<Dispositivo> dispositivi = new ArrayList<>();
        dispositivi.add(new Dispositivo("Arduino","Microcontrollore Atmel",new Data(2001,9,11)));
        dispositivi.add(new Dispositivo("Trasformatore","220V 12A",new Data(2001,9,11)));
        dispositivi.add(new Dispositivo("Multimetro","Voltometro e Amperometro",new Data(2001,9,11)));
        dispositivi.add(new Dispositivo("Sensore Umidità","Sensore I2C",new Data(2001,9,11)));
        
        Laboratorio laboratorio = new Laboratorio(docente0,tecnici,dispositivi);
       
        Cdl ingInfo = new Cdl("Ingegneria Informatica");
        Cdl informatica = new Cdl("Informatica");
        
        List<Studente> studenti = new ArrayList<>();
        studenti.add(new Studente("Alessandro","Amedei","6162943",ingInfo));
        studenti.add(new Studente("Alberto","Baldrati","6123123",ingInfo));
        studenti.add(new Studente("Matteo","Zoppini","6321321",ingInfo));
        studenti.add(new Studente("Gianmaria","Pandolfi","6000000",ingInfo));
        studenti.add(new Studente("Corso","Vignoli","6999999",ingInfo));
        studenti.add(new Studente("Paolo","Rossi","500000",informatica));
        
        Insegnamento ingegneriaDelSoftware = new Insegnamento(docente,studenti,"Ingegneria del Software",6,ingInfo);
        Insegnamento inteligenzaArtificiale = new Insegnamento(docente2,studenti,"Inteligenza Artificiale",6,ingInfo);
        
        Progetto p = new Progetto(docente);
        Progetto p2 = new Progetto(docente2);
        laboratorio.addProgetto(p);
        laboratorio.addProgetto(p2);
        
        studenti.get(0).richiediDispositivo(tecnici.get(0),dispositivi.get(0), new Data(2016,1,23));
        studenti.get(1).richiediDispositivo(tecnici.get(0),dispositivi.get(1), new Data(2019,1,25));
        studenti.get(2).richiediDispositivo(tecnici.get(1),dispositivi.get(2), new Data(2017,9,23));
       
        
        docente.registraElaborato(p,tecnici.get(0), "Elaborato", "Scheda arduino per registrazione temperatura", ingegneriaDelSoftware, 2);
        
        docente2.registraElaborato(p2,tecnici.get(1),"Elaborato AI","Bayes Native Intrusion Detector", inteligenzaArtificiale,4);
        
        p.getElaborati().get(1).concludiElaborato();
        
        
        for(Elaborato e: p.getElaborati()){
            e.produciDocumentazione();
        }
        
        for(Elaborato e: p2.getElaborati()){
            e.produciDocumentazione();
        }
        
               tecnici.get(0).notificaStudenteMancataConsegna(laboratorio);
 
    }
    
}

class Laboratorio{
    private Docente responsabile;
    private List<Tecnico> tecnici = new ArrayList<>();
    private List<Dispositivo> inventario = new ArrayList<>();
    private List<Progetto> progetti = new ArrayList<>();
    
    Laboratorio(Docente r,List<Tecnico> t,List<Dispositivo> i){
        responsabile = r;
        tecnici = t;
        inventario = i;
    }
    
    public void addProgetto(Progetto p){
            progetti.add(p);
        }
    
    public List<Dispositivo> getInventario(){
        return inventario;
    }
}

class Docente{
    private String nome;
    private String cognome;
    
    Docente(String n,String c){
        nome = n;
        cognome = c;
    }
    
    public void registraElaborato(Progetto progetto, Tecnico t,String nome,String programmaDiLavoro,Insegnamento insegnamento,int n_componenti_gruppo){
        List<List<Studente>> gruppi = identificaStudenti(n_componenti_gruppo,insegnamento);
        for(List<Studente> studentiGruppo : gruppi){
            progetto.addElaborato(new Elaborato(insegnamento,t,studentiGruppo,nome,programmaDiLavoro));
        }
    }
    
    private List<List<Studente>> identificaStudenti(int n_componenti_gruppo,Insegnamento insegnamento){
        List<List<Studente>> gruppi = new ArrayList<>();
        for (List<Studente> gruppo : gruppi)
            gruppo = new ArrayList<>();
       
        
        List<Studente> studenti = insegnamento.getListaStudenti();
        int size = studenti.size();
        
        int n_gruppi = (int) Math.ceil(size/n_componenti_gruppo);
        if(((float) size/n_componenti_gruppo)!=Math.round(size/n_componenti_gruppo))
            n_gruppi = (int) Math.ceil(size/n_componenti_gruppo)+1;
        
        
        for(int i=0 ; i < n_gruppi ; i++){
            List<Studente> gruppo = new ArrayList<>();
            for(int j=0; j < n_componenti_gruppo; j++){
                try{
                gruppo.add(studenti.get(i*(n_componenti_gruppo)+j));
                }catch(java.lang.IndexOutOfBoundsException e){}
                
            }
            gruppi.add(gruppo); 
        }
        
        return gruppi;
    }
    
    
    
}

class Tecnico{
    private String nome;
    private String cognome;
    
    Tecnico(String n,String c){
        nome = n;
        cognome = c;
    }
    
    public void assegnaDispositivo(Studente studente,Dispositivo dispositivo,Data dataScadenza){
        dispositivo.assegnaDispositivo(studente,dataScadenza);
    }
    
    private List<Dispositivo> individuaDispositiviMancanti(Laboratorio laboratorio){
        
        System.out.println("Il tecnico: " + nome + " : " + cognome + " sta individuando i dispositivi non consegnati");
        
        List<Dispositivo> dispositiviMancanti = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Data today = new Data(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        
        for(int i=0; i < laboratorio.getInventario().size(); i++ ){
            Data d = laboratorio.getInventario().get(i).getDataScadenza();
            if(d!=null){
            if(today.isLaterThen(laboratorio.getInventario().get(i).getDataScadenza())){
                dispositiviMancanti.add(laboratorio.getInventario().get(i));
            }
            }
        }
        
        return dispositiviMancanti;
    }
    
    public void notificaStudenteMancataConsegna(Laboratorio lab){
        List<Dispositivo> dispositiviMancanti = individuaDispositiviMancanti(lab);
        for(Dispositivo dispositivo:dispositiviMancanti){
            dispositivo.getStudente().notificaStudente("Devi riconsegnare l'oggetto: " + dispositivo.getNome() + " , è scaduto in data: "+ dispositivo.getDataScadenza().toString(),dispositivo);
        }
    }
    
}

class Cdl{
    private String nome;
    Cdl(String n){ nome = n; }
}

class Studente{
    private String nome;
    private String cognome;
    private String matricola;
    private Cdl cdl;
    
    
    Studente(String n,String c,String m,Cdl cd){
        nome = n;
        cognome = c;
        matricola = m;
        cdl = cd;
    }
    
    public void richiediDispositivo(Tecnico tecnico,Dispositivo dispositivo,Data dataScadenza){
        tecnico.assegnaDispositivo(this, dispositivo, dataScadenza);
    }
    
    public void consegnaDispositivo(Dispositivo dispositivo){
        System.out.println( System.lineSeparator() + "Lo studente:" + nome + " : " + cognome + " : " + matricola + System.lineSeparator() +
                "Ha riconsegnato l'oggetto: "+ dispositivo.getNome());
        
        dispositivo.dispositivoDisponibile();
    }
    
    public void notificaStudente(String message,Dispositivo dispositivo){
        System.out.println( System.lineSeparator() + "Lo studente:" + nome + " : " + cognome + " : " + matricola + System.lineSeparator() +
                "Ha ricevuto la seguente notifica: " + message + System.lineSeparator()
        );
        consegnaDispositivo(dispositivo);
        
    }
    
    public String getNome(){
        return nome;
    }
    
    public String getCognome(){
        return cognome;
    }
    
    public String getMatricola(){
        return matricola;
    }
    
}

class ProgrammaDottorato{
    private String nome;
    ProgrammaDottorato(String n){ nome = n; }
}

class Dottorando{
    private String nome;
    private String cognome;
    private String matricola;
    private List<Docente> advisor = new ArrayList<>();
    private ProgrammaDottorato programmaDottorato;
    
    Dottorando(String n,String c,String m,List<Docente> adv,ProgrammaDottorato pD){
        nome = n;
        cognome = c;
        matricola = m;
        advisor = adv;
        programmaDottorato = pD;
    }
}

class Assegnista{
    private String nome;
    private String cognome;
    private String matricola;
    private Docente responsabile;
    
    Assegnista(String n,String c,String m,Docente r){
        nome = n;
        cognome = c;
        matricola = m;
        responsabile = r;
    }
}

class Data{
    private int anno;
    private int mese;
    private int giorno;
    Data(int a,int m,int g){
        anno = a;
        mese = m;
        giorno = g;
    }
    public String toString(){
        return String.valueOf(giorno)+"/"+String.valueOf(mese)+"/"+String.valueOf(anno);
    }
    
    public int getYear(){ return anno; }
    public int getMonth(){ return mese; }
    public int getDay(){ return giorno; }
    
    public boolean isLaterThen(Data data){
        if(this.anno>data.getYear())
            return true;
        if(this.anno<data.getYear())
            return false;
        if(this.mese>data.getMonth())
            return true;
        if(this.mese<data.getMonth())
            return false;
        if(this.giorno>data.getDay())
            return true;
        return false;
    }
    
}

class Dispositivo{
    private String nome;
    private String descrizione;
    private Data dataAcquisto;
    private Studente studente;
    private Data dataScadenza;
    
    Dispositivo(String n,String d,Data dA){
        nome = n;
        descrizione = d;
        dataAcquisto = dA;
    }
    
    public void assegnaDispositivo(Studente s,Data d){
        studente = s;
        dataScadenza = d;
    }
    
    public void dispositivoDisponibile(){
        studente = null;
        dataScadenza = null;
    }
    
    public Data getDataScadenza(){
        return dataScadenza;
    }
    
    public Studente getStudente(){
        return studente;
    }
    
    public String getNome(){
        return nome;
    }
}

class Progetto{
    private Docente responsabile;
    private List<Elaborato> elaborati = new ArrayList<>();
    
    Progetto(Docente r){
        responsabile = r;
    }
    
    public void addElaborato(Elaborato e){
        elaborati.add(e);
    }
    public List<Elaborato> getElaborati(){
        return elaborati;
    }
}

class Insegnamento{
    private Docente docente;
    private List<Studente> studenti = new ArrayList<Studente>();
    private String nome;
    private int cfu;
    private Cdl cdl;
    
    Insegnamento(Docente d,List<Studente> ss,String n,int cf,Cdl cd){
        docente = d;
        studenti = ss;
        nome = n;
        cfu = cf;
        cdl = cd;
    }
    
    public List<Studente> getListaStudenti(){
        return studenti; //Si poteva implementare copia difensiva
    }
    
    public String getNome(){
        return nome;
    }
}

class Elaborato{
    
    private Insegnamento insegnamento;
    private String nome;
    private String programmaDiLavoro;
    private Tecnico tecnicoResponsabile;
    private List<Studente> studenti = new ArrayList<>();
    private List<Docente> docenti = new ArrayList<>();
    private List<Tecnico> tecnici = new ArrayList<>();
    private List<Dottorando> dottorandi = new ArrayList<>();
    private List<Assegnista> assegnisti = new ArrayList<>();
    private Data start;
    private Data end;
    
    Elaborato(Insegnamento i,Tecnico tr,List<Studente> s,String n,String pdl){
        insegnamento = i;
        tecnicoResponsabile = tr;
        studenti = s;
        nome = n;
        programmaDiLavoro = pdl;
        
        Calendar calendar = Calendar.getInstance();
        start = new Data(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }
    
    public void addTecnico(Tecnico t){ tecnici.add(t); }
    public void addDottorando(Dottorando d){ dottorandi.add(d); }
    public void addAssegnista(Assegnista a){ assegnisti.add(a); }
    public void concludiElaborato(){
         Calendar calendar = Calendar.getInstance();
         end = new Data(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
         
    }
    public void produciDocumentazione(){
        
        String studentiPartecipanti="";
        for(Studente studente : studenti){
            studentiPartecipanti +=  System.lineSeparator() +  "  " +  studente.getNome() + " : " + studente.getCognome() + " : " + studente.getMatricola();
        }
        
        String fine = "L'elaborato non è ancora concluso";
        if (end!=null)
            fine = end.toString();
        
        System.out.println(
                "Nome elaborato: " + nome + System.lineSeparator() +
                "Insegnamento: " + insegnamento.getNome() + System.lineSeparator() +
                "Programma: " + programmaDiLavoro + System.lineSeparator() +
                "Inizio: " + start.toString() + System.lineSeparator()+
                "Fine: " + fine + System.lineSeparator()+
                "Studenti partecipanti:" + studentiPartecipanti + System.lineSeparator()
        );
        
    }
    
    
}

