export abstract class User{
    
    private _id: string;
    protected _fname: string;
	protected _lname: string;
	protected _email: string;
	protected _password: string;
	protected _token: string;
  
    constructor(fname: string, lname: string, email: string, password: string) {
		this._fname = fname;
		this._lname = lname;
		this._email = email;
		this._password = password;
	}

	public setId(id:string): void{
		this._id = id;
	}
	public getId():string{
		return this._id;
	}
	public setFName(fname:string): void{
		this._fname = fname;
	}
	public getFName():string{
		return this._fname;
	}
	public setLName(lname:string): void{
		this._lname = lname;
	}
	public getLName():string{
		return this._lname;
	}
	public setEmail(email:string): void{
		this._email = email;
	}
	public getEmail():string{
		return this._email;
	}
	public setPassword(password:string): void{
		this._password = password;
	}
	public getPassword():string{
		return this._password;
	}
	public setToken (token :string): void{
		this._token = token;
	}
	public getToken(): string{
		return this._token;
	}
    abstract getType(): string;
}


