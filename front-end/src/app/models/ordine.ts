export interface Ordine {
  id: number;
  idUtente: number;
  data: string;
  stato: string;
  totalePagare: number;
  idMetodoPagamento: number;
}
