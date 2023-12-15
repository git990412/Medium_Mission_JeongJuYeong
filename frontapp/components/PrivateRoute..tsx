import { selectAuthState } from "@/_app/feature/auth";
import { useAppSelector } from "@/_app/hooks";
import { instance } from "@/config/axiosConfig";
import RsData from "@/types/rsData";
import { NextPage } from "next";
import { useParams, useRouter } from "next/navigation";

import { useEffect, useState } from 'react';

export const deniedAuth = (Component: NextPage | React.FC) => {
    const Auth = () => {
        const isLoggedIn = useAppSelector(selectAuthState);
        const router = useRouter();

        useEffect(() => {
            if (isLoggedIn) {
                router.push("/");
            }
        }, [isLoggedIn, router]);

        if (isLoggedIn) {
            return null;
        } else {
            return <Component />;
        }
    };

    return Auth;
};

export const accessAuth = (Component: NextPage | React.FC<{ params?: { index?: string } }>) => {
    const Auth = () => {
        const isLoggedIn = useAppSelector(selectAuthState);
        const router = useRouter();

        useEffect(() => {
            if (!isLoggedIn) {
                router.push("/");
            }
        }, [isLoggedIn, router]);

        if (!isLoggedIn) {
            return null;
        } else {
            return <Component />;
        }
    };

    return Auth;
};

export function accessAuthWithParams(Page: ({ params }: { params: { index?: string; }; }) => import("react").JSX.Element) {
    const Auth = () => {
        const isLoggedIn = useAppSelector(selectAuthState);
        const router = useRouter();
        const param = useParams();
        const index: string = param.index as string;

        const [isOwner, setIsOwner] = useState(false);

        useEffect(() => {
            instance.get(`/post/${index}/check`).then((res) => {
                const rsData: RsData = res.data;

                if (!isLoggedIn || !rsData.success) {
                    console.log(isLoggedIn, isOwner)
                    router.push("/");
                }
            })
        }, [isLoggedIn, router]);

        if (!isLoggedIn) {
            return null;
        } else {
            return <Page params={{ index: index }} />;
        }
    };

    return Auth;
}

