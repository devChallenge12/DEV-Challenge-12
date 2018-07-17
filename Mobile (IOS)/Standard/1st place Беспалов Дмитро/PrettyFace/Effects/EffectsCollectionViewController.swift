//
//  EffectsCollectionViewController.swift
//  PrettyFace
//
//  Copyright © 2018 DevChallenge. All rights reserved.
//

import UIKit

private let cellReuseIdentifier = "EffectCollectionViewCell"
private let headerReuseIdentifier = "EffectCollectionHeaderView"

protocol SelectedEffectDelegate {
    func didFinishPickingEffects(_ images: [UIImage])
}

class EffectCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var imageView: UIImageView!
}

class EffectCollectionHeaderView: UICollectionReusableView {
    @IBOutlet weak var titleLabel: UILabel!
}

class EffectsCollectionViewController: UICollectionViewController {
    
    var delegate: SelectedEffectDelegate?
    
    let collection: [(category: String, numberOfItems: Int)] = [("lips", 3),
                                                              ("glasses", 11),
                                                              ("mustache", 6)];
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // self.clearsSelectionOnViewWillAppear = false
    }

    // MARK: UICollectionViewDataSource

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        return collection.count
    }


    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return collection[section].numberOfItems
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: cellReuseIdentifier, for: indexPath) as? EffectCollectionViewCell
        else {
            fatalError("Dequeue error")
        }
        
        cell.imageView.image = UIImage(named: "\(collection[indexPath.section].category)\(indexPath.row + 1)")
        return cell
    }
    
    override func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        let headerView = collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: headerReuseIdentifier, for: indexPath)
        
        if let headerView = headerView as? EffectCollectionHeaderView {
            headerView.titleLabel.text = collection[indexPath.section].category.uppercased()
        }
        
        return headerView
    }
    // MARK: UICollectionViewDelegate

    
    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        guard let image = UIImage(named: "\(collection[indexPath.section].category)\(indexPath.row + 1)")
            else {
                return
        }
        
        dismiss(animated: true) {
            self.delegate?.didFinishPickingEffects([image])
        }
    }
    /*
    // Uncomment this method to specify if the specified item should be highlighted during tracking
    override func collectionView(_ collectionView: UICollectionView, shouldHighlightItemAt indexPath: IndexPath) -> Bool {
        return true
    }
    */

    
//     Uncomment this method to specify if the specified item should be selected
    override func collectionView(_ collectionView: UICollectionView, shouldSelectItemAt indexPath: IndexPath) -> Bool {
        return true
    }

}
