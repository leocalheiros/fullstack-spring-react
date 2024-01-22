import React from "react";

interface InputTextProps{
    style?: string;
    onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
    placeholder?: string;
    id?: string;
    value?: string;
}

export const InputText: React.FC<InputTextProps> = ({ style, ...outrasProps }: InputTextProps) => {
    return(
        <input
            type='text' 
            {...outrasProps}
            className={`${style} bg-gray-300 border px-5 py-2 rounded-lg text-gray-900`} />
    )
}