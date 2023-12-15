'use client'
import { accessAuth } from "@/components/PrivateRoute.";
import { instance } from "@/config/axiosConfig";
import Post from "@/types/Post";
import formatDate from "@/util/formatDate";
import { Link } from "@nextui-org/link";
import {
    getKeyValue,
    Pagination,
    Spinner,
    Table,
    TableBody,
    TableCell,
    TableColumn,
    TableHeader,
    TableRow
} from "@nextui-org/react";
import React from "react";
import useSWR from "swr";

const Page = () => {
    const [page, setPage] = React.useState(1);

    const { data, isLoading } = useSWR(`/post/myList?page=${page - 1}`, async (u) => {
        const res = await instance.get(u);
        const rsData = res.data;

        rsData.data.content = rsData.data.content.map((post: any) => {
            post.createDate = formatDate(new Date(post.createDate));
            return post;
        })

        return rsData.data;
    }, {
        keepPreviousData: true,
    });

    const pages = data?.totalPages;

    const loadingState = isLoading || data?.totalElements === 0 ? "loading" : "idle";

    return (
        <div className={"flex flex-col items-center"}>
            <h1 className={"font-bold text-4xl"}>내 글 목록</h1>
            <Table
                aria-label="Post Table"
                className={"mt-5"}
                bottomContent={
                    pages > 0 ? (
                        <div className="flex w-full justify-center">
                            <Pagination
                                isCompact
                                showControls
                                showShadow
                                color="primary"
                                page={page}
                                total={pages}
                                onChange={(page) => setPage(page)}
                            />
                        </div>
                    ) : null
                }
            >
                <TableHeader>
                    <TableColumn key="title">제목</TableColumn>
                    <TableColumn key="createDate">작성일</TableColumn>
                    <TableColumn key="username">작성자</TableColumn>
                </TableHeader>
                <TableBody
                    items={data?.content ?? []}
                    loadingContent={<Spinner />}
                    loadingState={loadingState}
                >
                    {(item) => (
                        <TableRow key={(item as Post).id}>
                            {(columnKey) => {
                                if (columnKey === 'username') {
                                    return (
                                        <TableCell>{getKeyValue((item as Post).member, columnKey)}</TableCell>
                                    )
                                } else if (columnKey === 'title') {
                                    return (
                                        <TableCell>
                                            <Link size={"sm"}
                                                href={`/post/${(item as Post).id}`}>{getKeyValue(item, columnKey)}</Link>
                                        </TableCell>
                                    )
                                }
                                return <TableCell>{getKeyValue(item, columnKey)}</TableCell>
                            }}
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        </div>
    );
}

export default accessAuth(Page);