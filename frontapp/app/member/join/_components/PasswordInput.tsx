import React from "react";
import {Input} from "@nextui-org/react";
import {EyeFilledIcon} from "./EyeFilledIcon";
import {EyeSlashFilledIcon} from "./EyeSlashFilledIcon";

interface Props {
    label?: string;
    placeholder?: string;
    className?: string;
    name?: string;
    errorMessage?: string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
    value?: string;
    isInvalid?: boolean;
}

export default function App(props: Props) {
    const [isVisible, setIsVisible] = React.useState(false);

    const toggleVisibility = () => setIsVisible(!isVisible);

    return (
        <Input
            label={props.label}
            variant="bordered"
            placeholder={props.placeholder}
            name={props.name}
            errorMessage={props.errorMessage}
            onChange={props.onChange}
            value={props.value}
            isInvalid={props.isInvalid}
            endContent={
                <button
                    className="focus:outline-none"
                    type="button"
                    onClick={toggleVisibility}
                >
                    {isVisible ? (
                        <EyeSlashFilledIcon className="text-2xl text-default-400 pointer-events-none"/>
                    ) : (
                        <EyeFilledIcon className="text-2xl text-default-400 pointer-events-none"/>
                    )}
                </button>
            }
            type={isVisible ? "text" : "password"}
            className={props.className}
        />
    );
}
