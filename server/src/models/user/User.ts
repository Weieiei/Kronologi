export abstract class User{
    private id: string;
	protected fname: string;
	protected lname: string;
	protected email: string;
	protected password: string;
	protected token: string;
  
    constructor(fname: string, lname: string, email: string, password: string) {
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.password = password;
	}

	public setId(id:string): void{
		this.id = id;
	}
	public getId():string{
		return this.id;
	}
	public setFName(fname:string): void{
		this.fname = fname;
	}
	public getFName():string{
		return this.fname;
	}
	public setLName(lname:string): void{
		this.lname = lname;
	}
	public getLName():string{
		return this.lname;
	}
	public setEmail(email:string): void{
		this.email = email;
	}
	public getEmail():string{
		return this.email;
	}
	public setPassword(password:string): void{
		this.password = password;
	}
	public getPassword():string{
		return this.password;
	}
	public setToken (token :string): void{
		this.token = token;
	}
	public getToken(): string{
		return this.token;
	}
    	abstract getType(): string;
}


