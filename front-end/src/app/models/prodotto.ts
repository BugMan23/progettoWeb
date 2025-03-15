export interface Prodotto {
  id: number;
  nome: string;
  marca: string;
  colore: string;
  prezzo: number;
  descrizione: string;
  scontato: boolean;
  urlImage: string;
  idCategoria: number;
}
