import React, { useState } from "react";
import { Form, Input, Button, InputNumber, Modal } from "antd";

const layout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
};

const DecisionForm: React.FC = () => {
    const [visible, setVisible] = useState(false);
    const [response, setResponse] = useState<any>({});

    const onFinish = async (values: any) => {
        const requestOptions = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(values),
        };

        try {
            const response = await fetch(
                "http://localhost:8080/decision-api/v1/decisions",
                requestOptions
            );
            const data = await response.json();
            setResponse(data);
            setVisible(true);
        } catch (error) {
            console.error("Error:", error);
        }
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log("Failed:", errorInfo);
    };

    const handleOk = () => {
        setVisible(false);
    };

    return (
        <div
            style={{
                display: "flex",
                justifyContent: "center",
                minHeight: "100vh",
                backgroundColor: "#f0f0f0",
            }}
        >
            <div
                style={{
                    maxWidth: "400px",
                    height: "300px",
                    width: "100%",
                    padding: "20px",
                    borderRadius: "8px",
                    backgroundColor: "#fff",
                    marginTop: "50px",
                }}
            >
                <div style={{ margin: "20px 0" }}>
                    <Form
                        {...layout}
                        name="basic"
                        initialValues={{ remember: true }}
                        onFinish={onFinish}
                        onFinishFailed={onFinishFailed}
                        size="large"
                    >
                        <Form.Item
                            label="Personal Id"
                            name="id"
                            rules={[
                                { required: true, message: "Please input your Personal Id!" },
                                { len: 11, message: "Personal Id must be 11 characters!" },
                            ]}
                        >
                            <Input className="ant-input" />
                        </Form.Item>

                        <Form.Item
                            label="Amount"
                            name="amount"
                            rules={[
                                { required: true, message: "Please input your Amount!" },
                                {
                                    type: "number",
                                    min: 2500,
                                    max: 10000,
                                    message: "Amount must be between 2500 and 10000!",
                                },
                            ]}
                        >
                            <InputNumber className="ant-input" />
                        </Form.Item>

                        <Form.Item
                            label="Period"
                            name="period"
                            rules={[
                                { required: true, message: "Please input your Period!" },
                                {
                                    type: "number",
                                    min: 12,
                                    max: 60,
                                    message: "Period must be between 12 and 60!",
                                },
                            ]}
                        >
                            <InputNumber className="ant-input" />
                        </Form.Item>

                        <Form.Item wrapperCol={{ offset: 8, span: 1 }}>
                            <div style={{ textAlign: "center" }}>
                                <Button
                                    type="primary"
                                    htmlType="submit"
                                    style={{ backgroundColor: "#6f42c1", borderColor: "#6f42c1" }}
                                >
                                    Submit
                                </Button>
                            </div>
                        </Form.Item>
                    </Form>
                </div>
            </div>
            <Modal
                title="Decision Response"
                visible={visible}
                onOk={handleOk}
                cancelButtonProps={{ style: { display: "none" } }}
            >
                {response && (
                    <>
                        <p>
                            Status:
                            <span
                                className={`response-status ${
                                    response.status && response.status.toLowerCase()
                                }`}
                            >
                                {response.status}
                            </span>
                        </p>
                        <p className="response-field">
                            Amount:
                            <span className="response-value">{response.amount}</span>
                        </p>
                        <p className="response-field">
                            Period:
                            <span className="response-value">{response.period}</span>
                        </p>
                    </>
                )}
            </Modal>
        </div>
    );
};

export default DecisionForm;
