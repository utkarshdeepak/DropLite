import {Table, TableBody, TableCell, TableColumn, TableHeader, TableRow} from "@heroui/react";
import {useEffect, useState} from "react";


export default function FileList() {
    const [rows, setRows] = useState([]);

    const columns = [
        { key: "name", label: "Name" },
        { key: "createdOn", label: "Created On" },
        { key: "size", label: "File Size" },
        { key: "download", label: "Download" },
    ];

    const formatDate = (isoString) => {
        if (!isoString) return "N/A";
        const date = new Date(isoString);
        return new Intl.DateTimeFormat("en-US", {
            month: "short",
            day: "2-digit",
            year: "numeric",
        }).format(date);
    };

    useEffect(() => {
        fetch("http://localhost:8080/api/files")
            .then((res) => res.json())
            .then((data) => {
                const formattedRows = data.map((file, index) => ({
                    id: file.id,
                    name: file.name,
                    createdOn: formatDate(file.createdAt),
                    size: file.size ?? "N/A",
                }));
                setRows(formattedRows);
            })
            .catch((error) => {
                console.error("Failed to fetch files:", error);
            });
    }, []);


    return (
        <Table
            isHeaderSticky
            isVirtualized
            // maxTableHeight={90}
            rowHeight={40}
        >
            <TableHeader columns={columns}>
                {(column) => <TableColumn key={column.key}>{column.label}</TableColumn>}
            </TableHeader>
            <TableBody items={rows}>
                {(item) => (
                    <TableRow key={item.key}>
                        {(columnKey) =>
                            columnKey === "download" ? (
                                <TableCell>
                                    {item.id ? (
                                        <a
                                            href={`http://localhost:8080/api/files/${item.id}`}
                                            target="_blank"
                                            rel="noopener noreferrer"
                                            download
                                        >
                                            Download
                                        </a>
                                    ) : (
                                        "N/A"
                                    )}
                                </TableCell>
                            ) : (
                                <TableCell>{item[columnKey]}</TableCell>
                            )
                        }
                    </TableRow>
                )}
            </TableBody>
        </Table>
    );
}
