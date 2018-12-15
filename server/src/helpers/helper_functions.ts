const passwordRegex = /^(?=.*\d)(?=.*[a-zA-Z]).{6,30}$/;

export function validatePassword(password: string): object {

    let isValid = false;
    const errors = {
        password: ''
    };

    if (password.length < 6 || password.length > 30) {
        errors.password = 'Password must be between 6 and 30 characters.';
    }
    else if (!passwordRegex.test(password)) {
        errors.password = 'Password must contain at least 1 letter and 1 digit.';
    }
    else {
        isValid = true;
    }

    return {
        isValid,
        errors
    };

}
