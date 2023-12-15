import {Button} from "@nextui-org/button";
import {Modal, ModalBody, ModalContent, ModalFooter, ModalHeader} from "@nextui-org/modal";
import React from "react";
import {Link} from "@nextui-org/link";

interface Props {
    isOpen?: boolean;
    onOpenChange?: () => void;
}

export default function CompleteAlert(props: Props) {
    const {isOpen, onOpenChange} = props;
    const [modalPlacement, setModalPlacement] = React.useState("auto");

    return (
        <div className="flex flex-col gap-2">
            <Modal
                isOpen={isOpen}
                placement={"center"}
                onOpenChange={onOpenChange}
                isDismissable={false}
                hideCloseButton={true}
            >
                <ModalContent>
                    {(onClose) => (
                        <>
                            <ModalHeader className="flex flex-col gap-1">회원가입 완료</ModalHeader>
                            <ModalBody>
                                <p>
                                    로그인을 통해 서비스를 이용하실 수 있습니다.
                                </p>
                            </ModalBody>
                            <ModalFooter>
                                <Button as={Link} href={"/"} color="primary" onPress={onClose}>
                                    확인
                                </Button>
                            </ModalFooter>
                        </>
                    )}
                </ModalContent>
            </Modal>
        </div>
    );
}