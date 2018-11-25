export abstract class User{

	private _id: number;
	private _firstName: string;
	private _lastName: string;
	private _email: string;
	private _username: string;
	private _password: string;
	
	constructor(firstName: string , lastName: string, email: string, username: string, password: string) {
		this._firstName = firstName;
		this._lastName = lastName;
		this._email = email;
		this._username = username;
		this._password = password;
	}

	public getId(): number {
		return this._id;
	}

	public getFirstName(): string {
		return this._firstName;
	}

	public setFirstName(firstName: string) {
		this._firstName = firstName;
	}

	public getLastName(): string {
		return this._lastName;
	}

	public setLastName(lastName: string) {
		this._lastName = lastName;
	}

	public getEmail(): string {
		return this._email;
	}

	public setEmail(email: string) {
		this._email = email;
	}

	public getUsername(): string {
		return this._username;
	}

	public setUsername(username: string): void {
		this._username = username;
	}

	public getPassword(): string {
		return this._password;
	}

	public setPassword(password: string) {
		this._password = password;
	}

	abstract getType(): string;

}
