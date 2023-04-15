package com.example.lucianodsepulveda.apppasajero.model;

import android.content.Context;

import com.example.lucianodsepulveda.apppasajero.interfaces.ScannerQRCodeInterface;

import androidx.appcompat.app.AppCompatActivity;

public class ScannerQRCodeInteractor extends AppCompatActivity implements ScannerQRCodeInterface.Interactor {

    private ScannerQRCodeRepository scannerQRCodeRepository;
    public ScannerQRCodeInteractor(ScannerQRCodeInterface.Presenter presenter, Context mContext) {
        scannerQRCodeRepository = new ScannerQRCodeRepositoryImp(presenter, mContext);
    }

    @Override
    public String makeRequestLlegadaCole(String idLineaQr, String idParadaQr) {
        return scannerQRCodeRepository.makeRequestLlegadaColeApi(idLineaQr, idParadaQr);
    }
}
