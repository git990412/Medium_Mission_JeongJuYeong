"use client";

import * as React from "react";
import {NextUIProvider} from "@nextui-org/system";
import {useRouter} from 'next/navigation'
import {ThemeProvider as NextThemesProvider} from "next-themes";
import {ThemeProviderProps} from "next-themes/dist/types";
import {Provider} from "react-redux";
import store from "@/_app/reduces";
import {PersistGate} from "redux-persist/integration/react";
import {persistStore} from "redux-persist";

export interface ProvidersProps {
    children: React.ReactNode;
    themeProps?: ThemeProviderProps;
}

const persistor = persistStore(store);

export function Providers({children, themeProps}: ProvidersProps) {
    const router = useRouter();

    return (
        <Provider store={store}>
            <PersistGate loading={null} persistor={persistor}>
                <NextUIProvider navigate={router.push}>
                    <NextThemesProvider {...themeProps}>{children}</NextThemesProvider>
                </NextUIProvider>
            </PersistGate>
        </Provider>
    );
}
