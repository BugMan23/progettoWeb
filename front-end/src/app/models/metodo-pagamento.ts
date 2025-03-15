export interface MetodoPagamento {
  id: number;
  tipoPagamento: string;
  titolare: string;
  tipoCarta: string;
  numeroCarta: string;
  dataScadenza: string;
  cvv: string;
  idUtente: number;
}
