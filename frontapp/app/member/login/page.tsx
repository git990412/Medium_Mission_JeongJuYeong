'use client'
import { setAuthState } from "@/_app/feature/auth";
import PasswordInput from "@/app/member/join/_components/PasswordInput";
import { deniedAuth } from "@/components/PrivateRoute.";
import { instance } from "@/config/axiosConfig";
import RsData from "@/types/rsData";
import { Button } from "@nextui-org/button";
import { Input } from "@nextui-org/input";
import React, { ChangeEvent } from "react";
import { useDispatch } from "react-redux";

const Page = () => {
    const dispatch = useDispatch();

    const [inputVal, setInputVal] = React.useState({
        username: "",
        password: "",
    });

    const [error, setError] = React.useState({
        username: "",
        password: "",
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

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const data = Object.fromEntries(formData.entries());
        instance.post("/members/login", data).then((res) => {
            const rsData: RsData = res.data;

            if (rsData.fail) {
                setError({
                    ...error,
                    ...rsData.data
                })
            } else {
                dispatch(setAuthState({ state: true, member: rsData.data }));
            }
        })
    };

    return (
        <div className="flex flex-col items-center">
            <div className="font-bold text-6xl mt-10">Login</div>
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
                <Button color="primary" type={"submit"} className="w-full mt-2">
                    로그인
                </Button>
            </form>
        </div>
    )
}

export default deniedAuth(Page);