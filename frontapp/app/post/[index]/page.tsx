'use client'

import { selectUsername } from "@/_app/feature/auth";
import { useAppSelector } from "@/_app/hooks";
import { instance } from "@/config/axiosConfig";
import "@/styles/detailPage.css";
import Post from "@/types/Post";
import { Button, Card, CardBody, CardHeader, Chip, Divider, Textarea, User } from "@nextui-org/react";
import * as DOMPurify from "dompurify";
import Link from "next/link";
import { useEffect, useState } from "react";
import "react-quill/dist/quill.snow.css";

const CommentInput = (props: { loadPost: () => void, params: { index: string } }) => {
    const [comment, setComment] = useState('');

    return (
        <>
            <Textarea
                value={comment}
                onChange={(e) => {
                    setComment(e.target.value)
                }}
                label="댓글"
                placeholder="댓글을 입력해주세요"
                className="w-full mt-5"
            />
            <Button onClick={() => {
                instance.post(`/post/${props.params.index}/comment/write`, { body: comment }).then((res) => {
                    const rsData = res.data;

                    if (rsData.success) {
                        alert("등록되었습니다.");
                        props.loadPost();
                    }
                })

            }} className="float-right mt-2" color="primary">등록</Button>
        </>
    );
};

const Page = ({ params }: { params: { index: string } }) => {
    const username = useAppSelector(selectUsername);

    const loadPost = () => {
        instance.get(`/post/${params.index}`).then((res) => {
            const rsData = res.data;

            setPost(rsData.data);
        })
    }

    useEffect(() => {
        //조회수 증가
        instance.put(`/post/${params.index}/hit`)

        loadPost();
    }, [])

    const deletePost = () => {
        instance.delete(`/post/${params.index}/delete`).then((res) => {
            const rsData = res.data;

            if (rsData.success) {
                alert("삭제되었습니다.");
                window.location.href = "/";
            }
        })
    }

    const [post, setPost] = useState({} as Post);

    const [comment, setComment] = useState("")

    return (
        <div>
            <h1 className="font-bold text-2xl">{post.title}</h1>
            <Divider className="mt-2" />
            <div className="ql-snow mt-4">
                <div className="ql-editor">
                    <div dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(String(post?.body)) }}></div>
                </div>
            </div>

            {username === post.member?.username ?
                <div className="flex flex-row-reverse">
                    <Button className="mt-2 float-right" as={Link} href={`/post/${post.id}/modify`} color="primary">수정</Button>
                    <Button className="mt-2 mr-2 float-right" onClick={deletePost} color="primary">삭제</Button>
                </div>
                : null}
            <Divider className="mt-2" />
            {post.comments?.map((comment) => {
                return (
                    <Card className="w-full mt-5">
                        <CardHeader className="flex gap-3 justify-between">
                            <User name={comment.username} />
                            {username === comment.username ?
                                <Chip classNames={{ content: ["flex relative"] }}>
                                    <div className="hover:text-blue-700 hover:cursor-pointer">수정</div>
                                    <div className="px-2">|</div>
                                    <div className="hover:text-blue-700 hover:cursor-pointer">삭제</div>
                                </Chip>
                                : null}
                        </CardHeader>
                        <Divider />
                        <CardBody>
                            <div>{comment.body}</div>
                        </CardBody>
                    </Card>
                )
            })}
            <CommentInput loadPost={loadPost} params={params} />
        </div>
    )
}

export default Page