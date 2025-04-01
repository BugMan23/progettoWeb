export class User {
  id: number | undefined;
  nome: string;
  cognome : string;
  email: string;
  password?: string;
  ruolo?: boolean;

  constructor(nome : string, cognome : string, email : string, password?: string, ruolo?: boolean) {
    this.nome = nome;
    this.cognome = cognome;
    this.email = email;
    this.password = password;
    this.ruolo = ruolo;
  }
}
