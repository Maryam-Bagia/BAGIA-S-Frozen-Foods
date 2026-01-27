<?php

if($_SERVER['REQUEST_METHOD'] === 'POST'){
    $name = trim($_POST['name'] ?? '');
    $phone = trim($_POST['phone'] ?? '');
    $details = trim($_POST['details'] ?? '');
    $time = date('Y-m-d H:i:s');

    if($name === '' || $phone === '' || $details === ''){
        header('Location: index.html#order');
        exit;
    }

    $line = [$time, str_replace(["\r","\n"],[' ',' '],$name), $phone, str_replace(["\r","\n"],[' ',' '],$details)];
    $fp = fopen('orders.csv','a');
    if($fp){
        fputcsv($fp, $line);
        fclose($fp);
    }

    $msg = rawurlencode("Hi Bagia's Frozen Foods,\nI would like to place an order:\nName: $name\nPhone: $phone\nOrder: $details\n");
    $wa = "https://wa.me/918780448636?text={$msg}";
    header('Location: ' . $wa);
    exit;
} else {
    header('Location: index.html');
    exit;
}
?>