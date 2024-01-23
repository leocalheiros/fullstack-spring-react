'use client'

import { Template, RenderIfComponent, InputText, Button, FieldError, useNotification} from '@/components'
import { useState } from 'react'
import { LoginForm, formScheme, validationScheme } from './formScheme'
import { useFormik } from 'formik'
import { useAuth } from '@/resources'
import { useRouter } from 'next/navigation'
import { AccessToken, LoginCredentials, User } from '@/resources/user/user.resource'

export default function Login(){

    const [loading, setLoading] = useState<boolean>(false);
    const [newUserState, setNewUserState] = useState<boolean>(false);

    const auth = useAuth();
    const notification = useNotification();
    const router = useRouter();

    const { values, handleChange, handleSubmit, errors, resetForm} = useFormik<LoginForm>({
        initialValues: formScheme,
        validationSchema: validationScheme,
        onSubmit: onSubmit
    })

    async function onSubmit(values: LoginForm){
        if(!newUserState){
            const credentials: LoginCredentials = { email: values.email, password: values.password}
            try{
                const accessToken: AccessToken = await auth.authenticate(credentials);
                auth.initSession(accessToken);
                router.push("/galeria");
                router.refresh()
            }catch(error: any){
                const message = error?.message;
                notification.notify(message, "error");
            }
        } else{

        const user: User = { email: values.email, name: values.name, password: values.password}

        try{
            await auth.save(user);
            notification.notify("Created user!", "success")
            resetForm();
            setNewUserState(false);
        } catch(error: any){
            const message = error.message;
            notification.notify(message, "error");
            }
        }
    }

    return(
        <Template loading={loading}>
            <div className='flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8'>

                <div className='sm:mx-auto sm:w-full sm:max-w-sm'>
                    <h2 className='mt-10 text-center text-1xl font-bold leading-9 tracking-tight text-gray-900'>
                        {newUserState ? 'Create New User' : 'Login to your account'}
                    </h2>
                </div>

                <div className='mt-10 sm:mx-auto sm:w-full sm:max-w-sm'>
                    <form onSubmit={handleSubmit} className='space-y-2'>
                        <RenderIfComponent condition={newUserState}>
                            <div>
                                <label className='block text-sm font-medium leading-6 text-gray-900'>Name: </label>
                            </div>
                            <div className='mt-2'>
                                <InputText style='w-full' 
                                                id='name'
                                                value={values.name}
                                                onChange={handleChange}/>
                                <FieldError error={errors.name}/>
                            </div>
                        </RenderIfComponent>

                        <div>
                                <label className='block text-sm font-medium leading-6 text-gray-900'>Email: </label>
                            </div>
                            <div className='mt-2'>
                                <InputText style='w-full' 
                                                id='email'
                                                value={values.email}
                                                onChange={handleChange}/>
                                <FieldError error={errors.email}/>
                            </div>
                        
                        <div>
                            <label className='block text-sm font-medium leading-6 text-gray-900'>Password: </label>
                        </div>
                        <div className='mt-3'>
                            <InputText style='w-full'
                                type='password'
                                id='password' 
                                value={values.password}
                                onChange={handleChange}/>
                            <FieldError error={errors.password}/>
                        </div>

                        <RenderIfComponent condition={newUserState}>
                            <div>
                                <label className='block text-sm font-medium leading-6 text-gray-900'>Repeat Password: </label>
                            </div>
                            <div className='mt-3'>
                                <InputText style='w-full'
                                    type='password'
                                    id='passwordMatch' 
                                    value={values.passwordMatch}
                                    onChange={handleChange}/>
                                    <FieldError error={errors.passwordMatch}/>
                            </div>
                        </RenderIfComponent>

                        <div>
                            <RenderIfComponent condition={newUserState}>
                                <Button type='submit' style='bg-indigo-700 hover:bg-indigo-500' label='Save'/>
                                <Button type='button' style='bg-red-700 hover:bg-red-500 mx-2' label='Cancel' onClick={event => setNewUserState(false)}/>
                            </RenderIfComponent>

                            <RenderIfComponent condition={!newUserState}>
                                <Button type='submit' style='bg-indigo-700 hover:bg-indigo-500' label='Login' />
                                <Button type='button' style='bg-red-700 hover:bg-red-500 mx-2' label='Sign up' onClick={event => setNewUserState(true)}/>
                            </RenderIfComponent>
                        </div>
                    </form>
                </div>
                
            </div>
        </Template>
    )   
}