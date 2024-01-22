import * as Yup from 'yup'

export interface FormProps{
    name: string;
    tags: string
    file: string | Blob;
}


export const formScheme: FormProps = { name: '', tags: '', file: '' }

export const formValidationScheme = Yup.object().shape({
    name: Yup.string().trim()
    .required('Name is required'
    ).max(50, 'Name must be less than 500 characters'),

    tags: Yup.string().trim()
    .required('Tags are required')
    .max(50, 'Tags must be less than 50 characters'),

    file: Yup.mixed<Blob>().required('Select image to upload!').test('size', 'File size cannot ben higher than 10MB', (file) => {
        return file.size < 10000000;
    })
    .test('type', 'Accepted formats: JPEG, GIF or PNG', (file) => {
        return file.type == 'image/jpeg' || file.type == 'image/png' || file.type == 'image/gif';
    })
})