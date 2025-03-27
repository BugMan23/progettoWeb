export interface Ordine {
  id: number;
  idUtente: number;
  data: string;
  stato: string;
  totalePagare: number;
  idMetodoPagamento: number;

  statoCalcolato?: string;
  progressoPercentuale?: number;
  dataConsegnaPrevista?: string;
}
