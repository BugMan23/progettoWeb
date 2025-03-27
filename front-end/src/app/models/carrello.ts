export interface Carrello {
  id: number;
  idUtente: number;
  idProdotto: number;
  quantita: number;
  taglia: string;
  isOrdinato: boolean;
  rimosso: boolean;
}
