export interface Indirizzo {
  id: number;
  nomeVia: string;
  civico: string;
  citta: string;
  cap: string;
  provincia: string;
  regione: string;
  idUtente: number;
  attivo?:boolean;
}
