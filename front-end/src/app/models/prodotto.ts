export interface Prodotto {
  id: number;
  nome: string;
  marca: string;
  colore: string;
  prezzo: number;
  descrizione: string;
  scontato: boolean;
  percentualeSconto?: number;
  urlImage: string;
  idCategoria: number;
}
