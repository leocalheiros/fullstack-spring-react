import * as Yup from 'yup'

export interface LoginForm{
    name?: string;
    email: string;
    password: string;
    passwordMatch?: string;
}


export const validationScheme = Yup.object().shape({
    email: Yup.string().trim()
    .required('Email is required'
    ).email('Invalid email'),

    password: Yup.string().trim()
    .required('Password are required')
    .min(6, 'Password must have at least 6 characters!'),

    passwordMatch: Yup.string().oneOf([Yup.ref('password')], 'Password must match!')
})

export const formScheme: LoginForm = {email: "", name: "", password: "", passwordMatch: ""}