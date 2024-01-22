import React from 'react'

interface ButtonProps {
    style?: string;
    label?: string;
    onClick?: (event: any) => void;
    type?: "submit" | "button" | "reset" | undefined

}

export const Button: React.FC<ButtonProps> = ({ onClick, style, label, type }: ButtonProps) => {
    const buttonClasses = `text-white px-4 py-2 rounded-lg ${style}`;

    return (
        <button className={buttonClasses} onClick={onClick} type={type}>{label}</button>
    )
}