"use client";


import PasswordInput from "./_components/PasswordInput";
import {Button} from "@nextui-org/button";
import {Input} from "@nextui-org/input";
import {instance} from "@/config/axiosConfig";
import React, {ChangeEvent, useEffect} from "react";
import RsData from "@/types/rsData";
import {useDisclosure} from "@nextui-org/react";
import CompleteAlert from "@/app/member/join/_components/CompleteAlert";
import {deniedAuth} from "@/components/PrivateRoute.";

const JoinPage = () => {
    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (error.passwordConfirm !== "") {
            return;
        }

        const formData = new FormData(e.currentTarget);
        const data = Object.fromEntries(formData.entries());
        instance.post("/members", data).then((res) => {
            const rsData: RsData = res.data;
            if (rsData.fail) {
                setError({
                    ...error,
                    ...rsData.data
                })
            } else {
                onOpen();
            }
        })
    };

    const [inputVal, setInputVal] = React.useState({
        username: "",
        password: "",
        passwordConfirm: "",
    });

    const [error, setError] = React.useState({
        username: "",
        password: "",
        passwordConfirm: "",
    });

    const handleChange = (e: ChangeEvent) => {
        const target = e.target as HTMLInputElement;
        setInputVal({
            ...inputVal,
            [target.name]: target.value,
        });

        setError({
            ...error,
            [target.name]: ""
        })
    }

    useEffect(() => {
        if (inputVal.password !== inputVal.passwordConfirm) {
            setError({
                ...error,
                passwordConfirm: "비밀번호가 일치하지 않습니다.",
            });
        }
    }, [inputVal]);

    const {isOpen, onOpen, onOpenChange} = useDisclosure();

    return (
        <div className="flex flex-col items-center">
            <div className="font-bold text-6xl mt-10">Join</div>
            <form className="w-3/4 mt-10" onSubmit={handleSubmit}>
                <Input
                    type="text"
                    variant="bordered"
                    label="Username"
                    name={"username"}
                    onChange={handleChange}
                    value={inputVal.username}
                    isInvalid={error.username !== ""}
                    errorMessage={error.username !== "" ? error.username : undefined}
                />
                <PasswordInput
                    label={"Password"}
                    name={"password"}
                    className={"w-full mt-2"}
                    onChange={handleChange}
                    value={inputVal.password}
                    isInvalid={error.password !== ""}
                    errorMessage={error.password !== "" ? error.password : undefined}
                />
                <PasswordInput
                    label={"Password Confirm"}
                    name={"passwordConfirm"}
                    className={"w-full mt-2"}
                    onChange={handleChange}
                    value={inputVal.passwordConfirm}
                    isInvalid={error.passwordConfirm !== ""}
                    errorMessage={error.passwordConfirm !== "" ? error.passwordConfirm : undefined}
                />
                <Button color="primary" type={"submit"} className="w-full mt-2">
                    회원가입
                </Button>
            </form>
            <CompleteAlert isOpen={isOpen} onOpenChange={onOpenChange}/>
        </div>
    );
}

export default deniedAuth(JoinPage);